package net.superlucamon.luero.heros.keymanagement;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;


public enum KeyBindingValues {
    ABILITYKEY_1(KeyBindings.ABILITYKEY_1),
    ABILITYKEY_2(KeyBindings.ABILITYKEY_2),
    ABILITYKEY_3(KeyBindings.ABILITYKEY_3),
    ABILITYKEY_4(KeyBindings.ABILITYKEY_4);

    private final KeyMapping keyMapping;

    KeyBindingValues(KeyMapping keyMapping) {
        this.keyMapping = keyMapping;
    }

    public String getFormattedKey() {
        StringBuilder sb = new StringBuilder();

        KeyModifier modifier = keyMapping.getKeyModifier();
        if (modifier != KeyModifier.NONE) {
            sb.append(modifier.name()).append(" + ");
        }

        sb.append(keyMapping.getKey().getDisplayName().getString());
        return sb.toString();
    }

    public KeyMapping getKeyMapping() {
        return keyMapping;
    }
}
