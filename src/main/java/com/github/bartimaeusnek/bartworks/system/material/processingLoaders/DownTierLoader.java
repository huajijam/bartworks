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

package com.github.bartimaeusnek.bartworks.system.material.processingLoaders;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import gregtech.api.util.GT_Recipe;

import java.util.HashSet;
import java.util.Set;

public class DownTierLoader implements Runnable {
    @Override
    public void run() {
        for (GT_Recipe.GT_Recipe_Map map : GT_Recipe.GT_Recipe_Map.sMappings) {
            if (map == GT_Recipe.GT_Recipe_Map.sScannerFakeRecipes || map == GT_Recipe.GT_Recipe_Map.sReplicatorFakeRecipes || map == GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes || map == GT_Recipe.GT_Recipe_Map.sAssemblylineVisualRecipes || map == GT_Recipe.GT_Recipe_Map.sFusionRecipes || map == GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes)
                return;
            Set<GT_Recipe> newRecipes = new HashSet<>();
            Set<GT_Recipe> toRem = new HashSet<>();
            for (GT_Recipe recipe : map.mRecipeList) {
                if (recipe == null)
                    continue;
                if (recipe.mEUt > 128) {
                    toRem.add(recipe);
                    newRecipes.add(BW_Util.copyAndSetTierToNewRecipe(recipe, (byte) 2));
                }
            }
            map.mRecipeList.removeAll(toRem);
            map.mRecipeList.addAll(newRecipes);
        }
    }
}
