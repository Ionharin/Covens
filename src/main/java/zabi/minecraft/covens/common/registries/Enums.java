package zabi.minecraft.covens.common.registries;

public class Enums {
	public static enum EnumInfusion {
		
		OVERWORLD(0x00ff00, 100), NETHER(0xff0000, 80), END(0x0000ff, 120);
		
		private int color, initialPower;
		
		EnumInfusion(int color, int power) {
			this.color=color;
			this.initialPower = power;
		}
		
		public int color() {
			return this.color;
		}
		
		public int getPower() {
			return initialPower;
		}
	}
}
