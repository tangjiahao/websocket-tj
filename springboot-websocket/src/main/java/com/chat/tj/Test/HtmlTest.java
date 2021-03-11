package com.chat.tj.Test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

/**
 * @author tangjing
 * @date 2021/03/04 15:33
 */
public class HtmlTest {

    private static String getSummary(String content, String title, String newsSource) {
        content = content.replaceAll("&emsp;", "");
        Document document = Jsoup.parse(content);
        content = document.text();

        System.out.println(content);

        // 删除内容中的来源
        if (!StringUtils.isEmpty(newsSource)) {
            content = content.replace("来源：", "").replace("来源:", "")
                    .replace(newsSource, "");
        }
        // 删除内容中的标题
        if (!StringUtils.isEmpty(title)) {
            content = content.replace(title, "");
        }
        // 去掉所有空格（中文和英文空格）
        content = content.replaceAll(" ", "").replaceAll(" ", "");
        int contentLength = content.length();
        content = content.substring(0, Math.min(contentLength, 175));
        System.out.println(content);
        return content;
    }

    public static void main(String[] args) {
        System.out.println(1);
        String content = "<p>股票代码：300112&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;股票简称：万讯自控          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;深圳万讯自控股份有限公司  （深圳市南山区高新技术产业园北区三号路万讯自控大楼 1-6 层）          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;创业板向不特定对象发行  &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;可转换公司债券募集说明书  &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; （修订稿）          &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;保荐机构（主承销商）      &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; （深圳市福田区福田街道福华一路 111 号）  &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; 2020 年 11 月  深圳万讯自控股份有限公司&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;可转债募集说明书        &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;声 明    &emsp;&emsp; 本公司及全体董事、监事、高级管理人员承诺募集说明书及其他信息披露资  保证。任何与之相反的声明均属虚假不实陈述。    &emsp;&emsp; 根据《证券法》的规定，证券依法发行后，";
        getSummary(content, "货币发展标题", "东方财富网");
    }
}
