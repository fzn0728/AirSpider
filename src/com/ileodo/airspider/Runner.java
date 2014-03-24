package com.ileodo.airspider;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

	public static ArrayList<String> cpList = new ArrayList<>();
	public static DateGenerator dater;
	public static String type;
	
	/**
	 * initial runner according to the system arguments
	 * @param type data type to be queried.
	 */
	public static void initial(String type){
		String ddlNameList ="";
		if(type.equals("城市环境评价点"))
			ddlNameList = "东城东四;东城天坛;西城官园;西城万寿西宫;朝阳奥体中心;朝阳农展馆;海淀万柳;海淀北部新区;海淀北京植物园;丰台花园;丰台云岗;石景山古城;房山良乡;大兴黄村镇;亦庄开发区;通州新城;顺义新城;昌平镇;门头沟龙泉镇;平谷镇;怀柔镇;密云镇;延庆镇;";
		else if(type.equals("对照点及区域点"))
			ddlNameList = "昌平定陵;京西北八达岭;京东北密云水库;京东东高村;京东南永乐店;京南榆垡;京西南琉璃河;";
		else if(type.equals("交通污染控制点"))
			ddlNameList = "前门东大街;永定门内大街;西直门北大街;南三环西路;东四环北路;";
		for(String s: ddlNameList.split(";")){
			cpList.add(s);
		}
	}
	
	public static void main(String[] args) {
		if(args.length!=4){
			System.out.println("java -jar AirSpider.jar type StartDate EndDate fileName(e.g java -jar AirSpider.jar 城市环境评价点 2013-03-01 2013-03-05 /Users/a.csv)");
			return;
		}
		type = args[0];
		initial(type);
		dater = new DateGenerator(args[1], args[2]);
		Querier q = new Querier();

		String fileName = args[3];
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
			while (dater.hasNext()) {
				String date = dater.next();
				for (String name : cpList){
					System.out.println("[For: "+type+"\t"+ name+"\t"+date+"]: "+DateGenerator.now());
					String body = q.query(type, name, date);
					StringBuilder sb = new StringBuilder();
					sb.append(date+","+type+","+name+",");
					//首要污染物
					sb.append(Utils.getReg("(?<=<td style=\"width:390px\">).*?(?=</td></tr></table></div>)", body).replace(',', ' ')+",");
					//空气质量指数
					sb.append(Utils.getReg("(?<=class=\"table\"><tr><td style=\"width:200px\">).*?(?=</td><td style=\"width:200px\">)", body)+",");
					//空气质量级别
					sb.append(Utils.getReg("(?<=</td><td style=\"width:200px\">).*?(?=</td><td style=\"width:190px\">)", body)+",");
					//空气质量描述
					sb.append(Utils.getReg("(?<=<td style=\"width:190px\">).*?(?=</td>)", body)+"\n");
					out.write(sb.toString());
					System.out.print(sb.toString());
					out.flush();
				}
			}
			out.close();
		}
		catch (IOException iox) {
			System.out.println("Problemwriting" + fileName);
		}

	}

}
