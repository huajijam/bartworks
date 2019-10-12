/*
 * Copyright (c) 2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.neiHandler;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Ores;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_OreLayer;
import com.github.bartimaeusnek.bartworks.system.oregen.BW_WorldGenRoss128b;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.GregTech_API;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BW_NEI_OreHandler extends TemplateRecipeHandler {

    public BW_NEI_OreHandler() {
        if (!NEI_BW_Config.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(MainMod.MOD_ID, "NEIPlugins", "register-crafting-handler", MainMod.MOD_ID + "@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
//            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public void drawBackground(int recipe) {
        GuiDraw.drawRect(0, 0, 166, 65, 0x888888);
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(0,40,40,10),"quickanddirtyneihandler"));
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equalsIgnoreCase("quickanddirtyneihandler")) {
            for (int i = 0; i < Werkstoff.werkstoffHashMap.values().size(); i++) {
                Werkstoff w = Werkstoff.werkstoffHashMap.get((short)i);
                if (w == null || w == Werkstoff.default_null_Werkstoff)
                    continue;
                if (w.getGenerationFeatures().hasOres()) {
                    ItemStack result = w.get(OrePrefixes.ore);
                    TemplateRecipeHandler.CachedRecipe tmp = new TemplateRecipeHandler.CachedRecipe() {

                        PositionedStack stack = new PositionedStack(result, 0, 0);

                        @Override
                        public PositionedStack getResult() {
                            return this.stack;
                        }

                        @Override
                        public List<PositionedStack> getOtherStacks() {
                            ArrayList<PositionedStack> ret = new ArrayList<>();
                            for (int i = 0; i < BW_OreLayer.sList.size(); i++) {
                                if (BW_OreLayer.sList.get(i) instanceof BW_WorldGenRoss128b) {
                                    int baseMeta = result.getItemDamage();
                                    BW_WorldGenRoss128b worldGen = ((BW_WorldGenRoss128b) BW_OreLayer.sList.get(i));
                                    if (worldGen.mPrimaryMeta == baseMeta || worldGen.mSecondaryMeta == baseMeta || worldGen.mBetweenMeta == baseMeta || worldGen.mSporadicMeta == baseMeta) {
                                        ItemStack other;
                                        other = result.copy().setStackDisplayName(result.getDisplayName().replaceAll("Ore", "Vein"));
                                        this.stack = new PositionedStack(other, 83, 0);
                                        if (((worldGen.bwOres & 0b1000) != 0)) {
                                            other = result.copy();
                                            other.setItemDamage(worldGen.mPrimaryMeta);
                                            ret.add(new PositionedStack(other, 0, 12));
                                        } else {
                                            other = new ItemStack(GregTech_API.sBlockOres1);
                                            other.setItemDamage(worldGen.mPrimaryMeta);
                                            ret.add(new PositionedStack(other, 0, 12));
                                        }
                                        if (((worldGen.bwOres & 0b0100) != 0)) {
                                            other = result.copy();
                                            other.setItemDamage(worldGen.mSecondaryMeta);
                                            ret.add(new PositionedStack(other, 20, 12));
                                        } else {
                                            other = new ItemStack(GregTech_API.sBlockOres1);
                                            other.setItemDamage(worldGen.mSecondaryMeta);
                                            ret.add(new PositionedStack(other, 20, 12));
                                        }
                                        if (((worldGen.bwOres & 0b0010) != 0)) {
                                            other = result.copy();
                                            other.setItemDamage(worldGen.mBetweenMeta);
                                            ret.add(new PositionedStack(other, 40, 12));
                                        } else {
                                            other = new ItemStack(GregTech_API.sBlockOres1);
                                            other.setItemDamage(worldGen.mBetweenMeta);
                                            ret.add(new PositionedStack(other, 40, 12));
                                        }
                                        if (((worldGen.bwOres & 0b0001) != 0)) {
                                            other = result.copy();
                                            other.setItemDamage(worldGen.mSporadicMeta);
                                            ret.add(new PositionedStack(other, 60, 12));
                                        } else {
                                            other = new ItemStack(GregTech_API.sBlockOres1);
                                            other.setItemDamage(worldGen.mSporadicMeta);
                                            ret.add(new PositionedStack(other, 60, 12));
                                        }
                                        break;
                                    }
                                }
                            }
                            return ret;
                        }
                    };
                    boolean add = true;
                    for (TemplateRecipeHandler.CachedRecipe recipe: this.arecipes) {
                        if (recipe == null || recipe.getOtherStacks() == null || recipe.getOtherStacks().get(0) == null || recipe.getOtherStacks().get(0).item == null)
                            continue;
                        if (GT_Utility.areStacksEqual(recipe.getOtherStacks().get(0).item,tmp.getOtherStacks().get(0).item))
                            add = false;
                    }
                    if (add)
                        this.arecipes.add(tmp);
                }
            }
        } else super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void drawExtras(int recipe) {
    if ((recipe < this.arecipes.size()) && (this.arecipes.get(recipe).getOtherStacks().size() >= 4) ) {
        GuiDraw.drawString(ChatColorHelper.BOLD + "DIM:" + ChatColorHelper.RESET + " Ross128", 0, 40, 0, false);
        GuiDraw.drawString(ChatColorHelper.BOLD + "Primary:", 0, 50, 0, false);
        GuiDraw.drawString(this.arecipes.get(recipe).getOtherStacks().get(0).item.getDisplayName(), 0, 60, 0, false);
        GuiDraw.drawString(ChatColorHelper.BOLD + "Secondary:", 0, 70, 0, false);
        GuiDraw.drawString(this.arecipes.get(recipe).getOtherStacks().get(1).item.getDisplayName(), 0, 80, 0, false);
        GuiDraw.drawString(ChatColorHelper.BOLD + "InBetween:", 0, 90, 0, false);
        GuiDraw.drawString(this.arecipes.get(recipe).getOtherStacks().get(2).item.getDisplayName(), 0, 100, 0, false);
        GuiDraw.drawString(ChatColorHelper.BOLD + "Sporadic:", 0, 110, 0, false);
        GuiDraw.drawString(this.arecipes.get(recipe).getOtherStacks().get(3).item.getDisplayName(), 0, 120, 0, false);
    }
        super.drawExtras(recipe);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (Block.getBlockFromItem(result.getItem()) instanceof BW_MetaGenerated_Ores) {
            TemplateRecipeHandler.CachedRecipe tmp = new TemplateRecipeHandler.CachedRecipe() {

                PositionedStack stack = new PositionedStack(result, 0, 0);

                @Override
                public PositionedStack getResult() {
                    return this.stack;
                }

                @Override
                public List<PositionedStack> getOtherStacks() {
                    ArrayList<PositionedStack> ret = new ArrayList<>();
                    for (int i = 0; i < BW_OreLayer.sList.size(); i++) {
                        if (BW_OreLayer.sList.get(i) instanceof BW_WorldGenRoss128b) {
                            int baseMeta = result.getItemDamage();
                            BW_WorldGenRoss128b worldGen = ((BW_WorldGenRoss128b) BW_OreLayer.sList.get(i));
                            if (worldGen.mPrimaryMeta == baseMeta || worldGen.mSecondaryMeta == baseMeta || worldGen.mBetweenMeta == baseMeta || worldGen.mSporadicMeta == baseMeta) {
                                ItemStack other;
                                other = result.copy().setStackDisplayName(result.getDisplayName().replaceAll("Ore", "Vein"));
                                this.stack = new PositionedStack(other, 83, 0);
                                if (((worldGen.bwOres & 0b1000) != 0)) {
                                    other = result.copy();
                                    other.setItemDamage(worldGen.mPrimaryMeta);
                                    ret.add(new PositionedStack(other, 0, 12));
                                } else {
                                    other = new ItemStack(GregTech_API.sBlockOres1);
                                    other.setItemDamage(worldGen.mPrimaryMeta);
                                    ret.add(new PositionedStack(other, 0, 12));
                                }
                                if (((worldGen.bwOres & 0b0100) != 0)) {
                                    other = result.copy();
                                    other.setItemDamage(worldGen.mSecondaryMeta);
                                    ret.add(new PositionedStack(other, 20, 12));
                                } else {
                                    other = new ItemStack(GregTech_API.sBlockOres1);
                                    other.setItemDamage(worldGen.mSecondaryMeta);
                                    ret.add(new PositionedStack(other, 20, 12));
                                }
                                if (((worldGen.bwOres & 0b0010) != 0)) {
                                    other = result.copy();
                                    other.setItemDamage(worldGen.mBetweenMeta);
                                    ret.add(new PositionedStack(other, 40, 12));
                                } else {
                                    other = new ItemStack(GregTech_API.sBlockOres1);
                                    other.setItemDamage(worldGen.mBetweenMeta);
                                    ret.add(new PositionedStack(other, 40, 12));
                                }
                                if (((worldGen.bwOres & 0b0001) != 0)) {
                                    other = result.copy();
                                    other.setItemDamage(worldGen.mSporadicMeta);
                                    ret.add(new PositionedStack(other, 60, 12));
                                } else {
                                    other = new ItemStack(GregTech_API.sBlockOres1);
                                    other.setItemDamage(worldGen.mSporadicMeta);
                                    ret.add(new PositionedStack(other, 60, 12));
                                }
                                break;
                            }
                        }
                    }
                    return ret;
                }
            };
            this.arecipes.add(tmp);
        }
    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/brewing_stand.png";
    }

    @Override
    public String getRecipeName() {
        return "BartWorks Ores";
    }
}
