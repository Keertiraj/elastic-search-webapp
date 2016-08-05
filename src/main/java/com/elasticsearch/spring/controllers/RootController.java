package com.elasticsearch.spring.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RootController {

	//private static final Logger logger = LoggerFactory.getLogger(RootController.class);
	
	private static final int RESULTS_PER_PAGE = 5;
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(@RequestParam(value = "query", required = false) String query, @RequestParam(value = "page", required = false) Integer page,HttpServletRequest request)
	{
		String viewName = "home";
		
		if(null != query)
			viewName = homePost(query,page,request);
		
		return viewName;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String homePost(@RequestParam("query") String query, @RequestParam(value = "page", required = false) Integer page, HttpServletRequest request)
		 {
		
        page = page == null ? 1: page.intValue();
        int from = (page-1)*RESULTS_PER_PAGE;

		SearchRequestBuilder srb = buildSearchRequest();

		QueryBuilder pastaQuery = QueryBuilders.queryStringQuery(query);
		
		/* Adding Fuzziness to the query */
		String[] columns = new String[]{"_all"};
		
		QueryBuilder fuzzyQuery = QueryBuilders.multiMatchQuery(query, columns).fuzziness(Fuzziness.AUTO);

	
		//srb.setQuery(pastaQuery).setSize(RESULTS_PER_PAGE).setFrom(from);
		srb.setQuery(fuzzyQuery).setSize(RESULTS_PER_PAGE).setFrom(from);
		executeQuery(srb, request,page);
		
		request.setAttribute("from", from+1);
		request.setAttribute("query", query);
		request.setAttribute("page", page);

		return "home";
	}

	private static void executeQuery(SearchRequestBuilder srb, HttpServletRequest request,int page) {
		
		SearchResponse searchResponse = srb.execute().actionGet();

		ArrayList<Map<String, Object>> hitslist = new ArrayList<Map<String, Object>>();

		for (SearchHit hit : searchResponse.getHits()) {
			Map<String, Object> map = hit.getSource();
			map.toString();
			hitslist.add(map);
		}
		
		int total = (int)searchResponse.getHits().getTotalHits();
        request.setAttribute("total", total);
        int to = page*RESULTS_PER_PAGE;
        to = to > total ? total:to;
		request.setAttribute("names", hitslist);
		int endPage = total%RESULTS_PER_PAGE == 0 ? (total/RESULTS_PER_PAGE): ((total/RESULTS_PER_PAGE)+1);
		request.setAttribute("endpage", endPage);
		request.setAttribute("to", to);

	}

	private static SearchRequestBuilder buildSearchRequest() {
		Client client = null;
		try {
			client = TransportClient.builder().build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return client.prepareSearch("ecommerce").setTypes("product")
				.setFetchSource(new String[] { "name", "description" }, null);

	}

}
