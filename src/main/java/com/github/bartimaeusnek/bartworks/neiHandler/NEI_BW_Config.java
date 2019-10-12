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

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;

@Optional.Interface(iface = "codechicken.nei.api.API", modid = "NotEnoughItems")
public class NEI_BW_Config implements IConfigureNEI {

    public static boolean sIsAdded = true;

    public void loadConfig() {
        API.hideItem(new ItemStack(ItemRegistry.TAB));
        API.hideItem(new ItemStack(FluidLoader.bioFluidBlock));
        API.hideItem(new ItemStack(ItemRegistry.bw_fake_glasses));
        NEI_BW_Config.sIsAdded = false;
        new BW_NEI_OreHandler();
        new BW_NEI_BioVatHandler(BWRecipes.instance.getMappingsFor(BWRecipes.BACTERIALVATBYTE));
        new BW_NEI_BioLabHandler(BWRecipes.instance.getMappingsFor(BWRecipes.BIOLABBYTE));
        NEI_BW_Config.sIsAdded = true;
    }

    @Optional.Method(modid = "NotEnoughItems")
    public String getName() {
        return "BartWorks NEI Plugin";
    }

    @Optional.Method(modid = "NotEnoughItems")
    public String getVersion() {
        return MainMod.APIVERSION;
    }
}