/**
 * ��Ŀ������зֽ⣬ʹ����Ϊ6�ı���
 * */
public class MoneyCombination {
	
	public static void main(String[] args) {
		// ��ֵ
		double M1 = 1, M5 = 5, M10 = 10, M20 = 20, M50 = 50, M100 = 100;
		// Ŀ����
		double target = 1314;

		double Max1 = Math.ceil(target / M1);
		double Max5 = Math.ceil(target / M5);
		double Max10 = Math.ceil(target / M10);
		double Max20 = Math.ceil(target / M20);
		double Max50 = Math.ceil(target / M50);
		double Max100 = Math.ceil(target / M100);

		System.out.println("Max1:" + Max1 + "\t" + "Max5:" + Max5 + "\t"
				+ "Max10:" + Max10 + "\t" + "Max20:" + Max20 + "\t" + "Max50:"
				+ Max50 + "\t" + "Max100:" + Max100);

		int count = 1;

		for (int M1I = 1; M1I < Max1; M1I++) {
			if(M1I % 6 == 0){
				for (int M5I = 1; M5I < Max5; M5I++) {
					if(M5I % 6 == 0){
						for (int M10I = 1; M10I < Max10; M10I++) {
							if(M10I % 6 == 0){
								for (int M20I = 1; M20I < Max20; M20I++) {
									if(M20I % 6 == 0){
										for (int M50I = 1; M50I < Max50; M50I++) {
											if(M50I % 6 == 0){
												for (int M100I = 1; M100I < Max100; M100I++) {
													if (4 + 5 * M5I + 10 * M10I + 20 * 6 + 50
															* M50I + 100 * M100I == target && M100I % 6 == 0) {
														System.out.println(count + "��" + 
																"1Ԫ��" + 4 + "��\t" + 
																"5Ԫ��" + M5I + "��\t" + 
																"10Ԫ��" + M10I + "��\t" + 
																"20Ԫ��" + 6 + "��\t" + 
																"50Ԫ��" + M50I + "��\t" + 
																"100Ԫ��" + M100I + "��");
														count++;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

	}

}
