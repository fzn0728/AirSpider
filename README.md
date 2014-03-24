AirSpider
=========
###Description
This project used to query data from `http://jc.bjmemc.com.cn/AirQualityDaily/DataSearch.aspx`
supporting output file format: csv

本项目用来爬取“北京市环境保护监测中心”网站上的北京空气质量情况。数据来源：http://jc.bjmemc.com.cn/AirQualityDaily/DataSearch.aspx
支持的输出文件格式：csv

###Method
download the AirSpider.jar, run the command in the following format:
`java -jar AirSpider.jar type StartDate EndDate fileName`

例如

`java -jar AirSpider.jar 城市环境评价点 2013-03-01 2013-03-05 /Users/a.csv`

下载 AirSpider.jar，运行以下命令
`java -jar AirSpider.jar type StartDate EndDate fileName`

例如

`java -jar AirSpider.jar 城市环境评价点 2013-03-01 2013-03-05 /Users/a.csv`
