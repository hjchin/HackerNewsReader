package com.hackernews.reader.data;

import com.hackernews.reader.comment.model.CommentItem;
import com.hackernews.reader.news.model.NewsItem;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by HJ Chin on 3/12/2017.
 */

@SuppressWarnings("ALL")
public class FakeData {

    public static Map<Integer, NewsItem> newsItems = new LinkedHashMap<>();;
    public static Map<Integer, CommentItem> commentItems = new LinkedHashMap<>();;
    public static Map<Integer, Item> items = new LinkedHashMap<>();

    static{

        for(int i=0;i<399;i++){
            NewsItem newsItem = new NewsItem();
            newsItem.id = i;

            newsItem.by = "by"+newsItem.id;

            int descendantCount = (new Random()).nextInt(9)+1;
            newsItem.descendants = descendantCount;
            newsItem.kids = new int[descendantCount];
            for(int j=0;j<descendantCount;j++){
                newsItem.kids[j] = (400+j);
            }

            newsItem.score = 111;
            newsItem.time = new Date().getTime()/1000;
            newsItem.title = "title "+newsItem.id;
            newsItem.type = "story";
            newsItem.url = "http://www.google.com";

            items.put(newsItem.id, newsItem);
            newsItems.put(newsItem.id, newsItem);
        }

        for(int i=400;i<410;i++){
            CommentItem item = new CommentItem();
            item.id = i;
            item.by = "by"+i;
            item.kids = new int[]{400};
            item.parent = -1;
            item.text =  "this is comment text of id "+i;
            item.time = new Date().getTime()/1000;
            item.type = "comment";

            items.put(item.id, item);
            commentItems.put(item.id,item);
        }
    }

}
