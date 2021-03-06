package novamachina.exnihilosequentia.common.crafting.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import novamachina.exnihilosequentia.api.crafting.RecipeSerializer;
import novamachina.exnihilosequentia.api.crafting.crucible.CrucibleRecipe;
import novamachina.exnihilosequentia.common.init.ExNihiloBlocks;
import novamachina.exnihilosequentia.common.tileentity.crucible.CrucilbeTypeEnum;
import novamachina.exnihilosequentia.common.utility.FluidStackUtils;

public class CrucibleRecipeSerializer extends RecipeSerializer<CrucibleRecipe> {
    @Override
    public ItemStack getIcon() {
        return new ItemStack(ExNihiloBlocks.CRUCIBLE_FIRED.get());
    }

    @Override
    public CrucibleRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        int amount = buffer.readInt();
        FluidStack fluid = FluidStack.readFromPacket(buffer);
        CrucilbeTypeEnum type = buffer.readEnum(CrucilbeTypeEnum.class);
        return new CrucibleRecipe(recipeId, input, amount, fluid, type);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, CrucibleRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeInt(recipe.getAmount());
        recipe.getResultFluid().writeToPacket(buffer);
        buffer.writeEnum(recipe.getCrucibleType());
    }

    @Override
    protected CrucibleRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        int amount = json.get("amount").getAsInt();
        FluidStack fluid = FluidStackUtils.jsonDeserializeFluidStack(json.get("fluidResult").getAsJsonObject());
        CrucilbeTypeEnum typeEnum = CrucilbeTypeEnum.getTypeByName(json.get("crucibleType").getAsString());
        return new CrucibleRecipe(recipeId, input, amount, fluid, typeEnum);
    }
}
