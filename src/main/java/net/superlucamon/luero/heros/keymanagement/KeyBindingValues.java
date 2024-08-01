package net.superlucamon.luero.heros.keymanagement;

public enum KeyBindingValues {
    ABILITYKEY_1(KeyBindings.ABILITYKEY_1.getKey().getDisplayName().getString()),
    ABILITYKEY_2(KeyBindings.ABILITYKEY_2.getKey().getDisplayName().getString()),
    ABILITYKEY_3(KeyBindings.ABILITYKEY_3.getKey().getDisplayName().getString()),
    ABILITYKEY_4(KeyBindings.ABILITYKEY_4.getKey().getDisplayName().getString());
    private final String value;

    KeyBindingValues(String translationKey) {
        this.value = translationKey;
    }
    public String getTranslationKey() {
        return value;
    }
}
