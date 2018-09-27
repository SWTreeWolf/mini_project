package minproject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.imageio.ImageIO;
import org.jsoup.*;
import org.jsoup.nodes.*;

public class Crawling {
	String url = "https://movie.naver.com/movie/running/current.nhn";
	Image img = null;
	String root = System.getProperty("user.dir");
	ArrayList<String[]> arr = new ArrayList<>();
	Document doc = null;
	URL uri = null;
	BufferedImage image= null;
	File file = null;
	final int max = 8;	// 받아오는 영화 수
	
	public Crawling() {
		try {
			doc = Jsoup.connect(url).get();
			Element el = doc.getElementsByClass("lst_detail_t1").get(0);
			//  i=영화 수
			for(int i=0; i<max; i++) {
				String url_detail = "https://movie.naver.com"+el.getElementsByTag("li").get(i).getElementsByClass("thumb").get(0).getElementsByTag("a").get(0).attr("href");
				
				//String[] 0 =영화명 , 1=장르, 2=러닝타임, 3=개봉일, 4=출연 (맥시멈 두명) 5=감독, 6=연령 7=예매율 8=줄거리
				String[] str =new String[9];
				
				//Element = 노드 접근 축약식
				Element el_tit1= el.getElementsByTag("li").get(i).getElementsByClass("tit_t1").get(2);
				Element el_tit2= el.getElementsByTag("li").get(i).getElementsByClass("tit_t2").get(0);
				Element el_tit3= el.getElementsByTag("li").get(i).getElementsByClass("tit_t3").get(0);
				
				//doc_story = 줄거리에 대한 내용으로 참조 문서값 변경
				Document doc_story = Jsoup.connect(url_detail).get();
				Element el_story = doc_story.getElementsByClass("story_area").get(0);
				
				
				if(el_story.children().hasClass("h_tx_story")) {
					str[8]=el_story.getElementsByClass("h_tx_story").text();
					str[8]+=el_story.getElementsByClass("con_tx").text();
					
				}else {
					str[8]=el_story.getElementsByClass("con_tx").text();
				}
				
				//영화명
				str[0] =el.getElementsByTag("li").get(i).getElementsByClass("tit").get(0).getElementsByTag("a").text();
				
				//장르 str[1]| 러닝타임str[2] | 개봉일str[3]
				String split = el_tit1.nextElementSibling().getElementsByTag("dd").text();

				str[1]=split.split("\\|")[0];
				str[2]=split.split("\\|")[1];
				str[3]=split.split("\\|")[2];
				
				//감독
				for(int j=0; j<el_tit2.nextElementSibling().getElementsByTag("a").size() && j<1; j++ ) {
					str[5]=el_tit2.nextElementSibling().getElementsByTag("a").get(j).text();
					
				}
				//출연
				for(int k=0; k<el_tit3.nextElementSibling().getElementsByTag("a").size() && k<2; k++ ) {

					if(k==1) {
						str[4]+=el_tit3.nextElementSibling().getElementsByTag("a").get(k).text();
						break;
					}
					if(1==el_tit3.nextElementSibling().getElementsByTag("a").size()) {
						str[4]=el_tit3.nextElementSibling().getElementsByTag("a").get(k).text();
						break;
					}
					str[4]=el_tit3.nextElementSibling().getElementsByTag("a").get(k).text()+",";
				}

				//연령
				
				str[6]=el.getElementsByClass("tit").get(i).getElementsByTag("span").text();
				
				
				str[7]=el.getElementsByTag("li").get(i).getElementsByClass("tit_t1").get(1).nextElementSibling().getElementsByTag("dd").get(0).getElementsByClass("star_t1 b_star").get(0).text();
				
				String link =el.getElementsByTag("li").get(i).getElementsByClass("thumb").get(0).getElementsByTag("img").get(0).attr("src"); 
				uri = new URL(link);
				image = ImageIO.read(uri);
				String folder = root+"\\src\\minproject\\img\\";
				
				//폴더가 없을 경우 폴더 생성
				if(!(new File(folder).isDirectory())) {
					new File(folder).mkdir();
				}
				// 폴더 생성후 파일 다운
				file = new File(folder+"movie"+i+".jpg");
				
				// 이미지 크기 늘리기
				int destWidth = 150;
				int destHeight = 200;
				
				Image imgTarget = image.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
				int pixels[] = new int[destWidth * destHeight]; 
				PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth); 
				try {
			        pg.grabPixels();
			    } catch (InterruptedException e) {
			        throw new IOException(e.getMessage());
			    } 
			    BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB); 
			    destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
			    ImageIO.write(destImg, "jpg", file);
			    
				
				//ImageIO.write(image, "jpg", file);
				arr.add(str);

			}//end for

		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}//end constructor
	
	

}
