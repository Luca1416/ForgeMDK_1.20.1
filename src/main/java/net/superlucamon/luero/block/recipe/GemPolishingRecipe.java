package net.superlucamon.luero.block.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.superlucamon.luero.Main;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GemPolishingRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final List<Integer> counts; // 💡 Add this


    public GemPolishingRecipe(NonNullList<Ingredient> inputItems, List<Integer> counts, ItemStack output, ResourceLocation id) {
        this.inputItems = inputItems;
        this.counts = counts;
        this.output = output;
        this.id = id;
    }

    /*@Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }

        return inputItems.get(0).test(pContainer.getItem(0));
    }
    */
    @Override
    public boolean matches(SimpleContainer container, Level level) {
        if (level.isClientSide()) return false;

        // Match input ingredients to slot 0 and slot 2 (not 0 and 1)
        return inputItems.get(0).test(container.getItem(0))
                && inputItems.get(1).test(container.getItem(2));
    }




    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public List<Integer> getCounts() {
        return counts;
    }


    public static class Type implements RecipeType<GemPolishingRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "gem_polishing";
    }

    public static class Serializer implements RecipeSerializer<GemPolishingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(Main.MOD_ID, "gem_polishing");

        @Override
        public GemPolishingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "output"));

            JsonArray ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
            List<Integer> counts = new ArrayList<>();

            for (int i = 0; i < ingredients.size(); i++) {
                JsonObject ingredientObj = ingredients.get(i).getAsJsonObject();
                inputs.set(i, Ingredient.fromJson(ingredientObj));
                counts.add(GsonHelper.getAsInt(ingredientObj, "count", 1)); // default to 1
            }
            return new GemPolishingRecipe(inputs, counts, output, pRecipeId);
        }

        @Override
        public @Nullable GemPolishingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            int inputCount = pBuffer.readInt();
            NonNullList<Ingredient> inputs = NonNullList.withSize(inputCount, Ingredient.EMPTY);
            List<Integer> counts = new ArrayList<>();

            for (int i = 0; i < inputCount; i++) {
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
                counts.add(pBuffer.readInt()); // 🔄 read count for each ingredient
            }

            ItemStack output = pBuffer.readItem();
            return new GemPolishingRecipe(inputs, counts, output, pRecipeId);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, GemPolishingRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getIngredients().size());

            for (int i = 0; i < pRecipe.getIngredients().size(); i++) {
                pRecipe.getIngredients().get(i).toNetwork(pBuffer);
                pBuffer.writeInt(pRecipe.getCounts().get(i)); // 📝 write count for each ingredient
            }

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }

    }
}