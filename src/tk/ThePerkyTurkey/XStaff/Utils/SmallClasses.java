package tk.ThePerkyTurkey.XStaff.Utils;

public class SmallClasses {
	
	public static class Trivalue {
		
		private String a, b, c;
	
		public Trivalue(String a, String b, String c) {
			this.a = a;
			this.b = b;
			this.c = c;
		}
		
		public String getA() {
			return a;
		}
		
		public String getB() {
			return b;
		}
		
		public String getC() {
			return c;
		}
	}
	
	public static class StaffMember {
		
		private String name;
		
		public StaffMember(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}

}
