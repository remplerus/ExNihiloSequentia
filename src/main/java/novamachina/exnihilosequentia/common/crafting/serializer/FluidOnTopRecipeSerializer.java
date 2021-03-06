package novamachina.exnihilosequentia.common.crafting.serializer;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import novamachina.exnihilosequentia.api.crafting.RecipeSerializer;
import novamachina.exnihilosequentia.api.crafting.fluidontop.FluidOnTopRecipe;
import novamachina.exnihilosequentia.common.init.ExNihiloBlocks;
import novamachina.exnihilosequentia.common.utility.FluidStackUtils;

public class FluidOnTopRecipeSerializer extends RecipeSerializer<FluidOnTopRecipe> {
    @Override
    public ItemStack getIcon() {
        return new ItemStack(ExNihiloBlocks.BARREL_WOOD.get());
    }

    @Override
    public FluidOnTopRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        FluidStack fluidInTank = FluidStack.readFromPacket(buffer);
        FluidStack fluidOnTop = FluidStack.readFromPacket(buffer);
        ItemStack result = buffer.readItem();
        return new FluidOnTopRecipe(recipeId, fluidInTank, fluidOnTop, result);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, FluidOnTopRecipe recipe) {
        recipe.getFluidInTank().writeToPacket(buffer);
        recipe.getFluidOnTop().writeToPacket(buffer);
        buffer.writeItem(recipe.getResultItem());
    }

    @Override
    protected FluidOnTopRecipe readFromJson(ResourceLocation recipeId, JsonObject json) {
        FluidStack fluidInTank = FluidStackUtils.jsonDeserializeFluidStack(json.get("fluidInTank").getAsJsonObject());
        FluidStack fluidOnTop = FluidStackUtils.jsonDeserializeFluidStack(json.get("fluidOnTop").getAsJsonObject());
        ItemStack result = readOutput(json.get("result"));
        return new FluidOnTopRecipe(recipeId, fluidInTank, fluidOnTop, result);
    }
}
