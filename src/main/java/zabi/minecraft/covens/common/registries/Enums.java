package zabi.minecraft.covens.common.registries;

public class Enums {
	public static enum EnumInfusion {
		
		OVERWORLD(0x00ff00), NETHER(0xff0000), END(0x0000ff);
		
		private int color;
		
		EnumInfusion(int color) {
			this.color=color;
		}
		
		public int color() {
			return this.color;
		}
	}
}
