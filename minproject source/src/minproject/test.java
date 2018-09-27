package minproject;

public class test {

	public static void main(String[] args) {
		//크롤링 사용법
		//프로젝트 우클릭 => properties
		//java path build
		//libary -> lib -> jsoup 외부 소스 등록
		//window -> properties
		// java -> complier-> errors/warning
		// "Forbidden reference (access rules)" -> "ignore"
		// "Discouraged reference (access rules)" -> "ignore"
		Crawling craw = new Crawling();
		for(String[] i: craw.arr) {
				System.out.println("영화명 : "+i[0]);
				System.out.println("장르 : "+i[1]);
				System.out.println("러닝타임 : "+i[2]);
				System.out.println("개봉일 : "+i[3]);
				System.out.println("출연 : "+i[4]);
				System.out.println("감독 : "+i[5]);
				System.out.println("연령 : "+i[6]);
				System.out.println("예매율 : "+i[7]);
				System.out.println("줄거리 : "+i[8]);
				System.out.println();
		}

	}

}
