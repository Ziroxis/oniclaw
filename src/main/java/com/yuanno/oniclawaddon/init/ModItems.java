package com.yuanno.oniclawaddon.init;

import com.yuanno.oniclawaddon.items.GryphonItem;
import com.yuanno.oniclawaddon.items.KaidoWeaponItem;
import com.yuanno.oniclawaddon.items.NapoleonItem;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;


public class ModItems {

    public static final RegistryObject<CoreSwordItem> GRYPHON = WyRegistry.registerItem("Gryphon", () -> new GryphonItem().setRepairIngredient(Ingredient.of(Items.IRON_INGOT)));
    public static final RegistryObject<CoreSwordItem> KAIDO_WEAPON = WyRegistry.registerItem("Kaido Weapon", () -> new KaidoWeaponItem().setRepairIngredient(Ingredient.of(Items.IRON_INGOT)));
    public static final RegistryObject<CoreSwordItem> KIKOKU = WyRegistry.registerItem("Kikoku Blade", () -> new CoreSwordItem(5, 180).setRepairIngredient(Ingredient.of(Items.IRON_INGOT)).setBlunt());
    public static final RegistryObject<CoreSwordItem> KURO_CLAWS = WyRegistry.registerItem("Kuro Claws", () -> new CoreSwordItem(5, 180).setRepairIngredient(Ingredient.of(Items.IRON_INGOT)).setBlunt());
    public static final RegistryObject<CoreSwordItem> MORGAN_AXE = WyRegistry.registerItem("Morgan Axe", () -> new CoreSwordItem(5, 180).setRepairIngredient(Ingredient.of(Items.IRON_INGOT)).setBlunt());
    public static final RegistryObject<CoreSwordItem> NAPOLEON = WyRegistry.registerItem("Napoleon", () -> new NapoleonItem().setRepairIngredient(Ingredient.of(Items.IRON_INGOT)).setBlunt());
    public static void register(IEventBus eventBus)
    {
    }
}
