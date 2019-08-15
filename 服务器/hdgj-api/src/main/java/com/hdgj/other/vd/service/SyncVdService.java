 package com.hdgj.other.vd.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hdgj.entity.AttrValue;
import com.hdgj.entity.ModelAttr;
import com.hdgj.entity.repository.AttrValueRepository;
import com.hdgj.entity.repository.ModelAttrRepository;
import com.hdgj.entity.repository.SkuAttrRepository;
import com.hdgj.other.vd.api.ProductService;
import com.weidian.open.sdk.exception.OpenException;

@Service
public class SyncVdService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SkuAttrRepository SkuAttrResponse;
	
	@Autowired
	private ModelAttrRepository modelAttrRepository;
	
	@Autowired
	private AttrValueRepository attrValueRepository;
	
	public void syncVdSkuAttr(){
		String response = "";
		try {
			response = productService.vdianSkuAttrsGet();
		} catch (OpenException e) {
			e.printStackTrace();
		}
		JSONObject res = JSON.parseObject(response);
		String status = res.getJSONObject("status").getString("status_code");
		
		JSONArray attrList = res.getJSONObject("result").getJSONArray("attr_list");
		Iterator ite = attrList.iterator();
		System.out.println("attr_list:" + attrList.toJSONString());

		
		while(ite.hasNext()){
			//型号属性对象
			JSONObject modelAttr = (JSONObject) ite.next();
			
			/**
			 * 同步属性值
			 */
			JSONArray attrValues = modelAttr.getJSONArray("attr_values");
			Iterator attrvIterator = attrValues.iterator();
			while(attrvIterator.hasNext()){
				AttrValue attvObj = ((JSONObject) attrvIterator.next()).toJavaObject(AttrValue.class);
				Example<AttrValue> ex = Example.of(attvObj);

				String attrValue = attvObj.getAttrValue();
				Update update = new Update();
				update.set("attr_id", attvObj.getAttrId());
				update.set("attr_value", attrValue);
				mongoTemplate.upsert(
						Query.query(Criteria.where("attr_id").is(attvObj.getAttrId())),
						update,
						AttrValue.class);
			}		
			
			System.out.println("attr_values:" + attrValues.toJSONString());
			
			/**
			 * 同步属性型号
			 */
			String attrTitle = modelAttr.getString("attr_title");
			Query query = Query.query(Criteria.where("attr_title").in(attrTitle));
			Update update = new Update();
			update.set("attr_title", attrTitle);
			update.set("attr_values", attrValues.toJavaList(AttrValue.class));
			mongoTemplate.upsert(query, update, ModelAttr.class);	
		}
		
		
		List<ModelAttr> mas =  mongoTemplate.findAll(ModelAttr.class);
		List<AttrValue> avs = mongoTemplate.findAll(AttrValue.class);
		System.out.println("mas:" + mas);
		System.out.println("avs:" + avs);
	}	
	
	
	
	
	/**
	 * 同步微店商品
	 */
	public int syncVdProduct()throws Exception{
		int totalNum = 0;
		try {
			String response = productService.vdianItemListGet(1, 1, 30, null, 0, null);
			JSONObject res = JSONObject.parseObject(response);
			int status =  res.getJSONObject("status").getInteger("status_code");
			if(status == 0){
				totalNum = res.getJSONObject("result").getInteger("total_num");
			}
		} catch (OpenException e) {
			e.printStackTrace();
		}
		return totalNum;

		
	}
	
	public void test2(){
		String response = "";
		try {
			response = productService.vdianSkuAttrsGet();
		} catch (OpenException e) {
			e.printStackTrace();
		}
		JSONObject res = JSON.parseObject(response);
		String status = res.getJSONObject("status").getString("status_code");
		
		List<ModelAttr> mas= res.getJSONObject("result").getJSONArray("attr_list").toJavaList(ModelAttr.class);
		
		Criteria c = new Criteria();
		c.where("username").is("1040978586").and("password").is("13379959770");
		mongoTemplate.find(Query.query(c),ModelAttr.class);
		
	}
	
	public void test3(){
		ModelAttr m = new ModelAttr();
		m.setAttrTitle("120克/瓶");
		
		Example<ModelAttr> am = Example.of(m);
		//Optional ms = modelAttrRepository.findOne(am);
		//System.out.println(ms);
	}
	
	public void test4(){
		String response = "";
		try {
			response = productService.vdianSkuAttrsGet();
		} catch (OpenException e) {
			e.printStackTrace();
		}
		JSONObject res = JSON.parseObject(response);
		String status = res.getJSONObject("status").getString("status_code");
		
		List<ModelAttr> mas = res.getJSONObject("result").getJSONArray("attr_list").toJavaList(ModelAttr.class);
		
		List<AttrValue> attvs = new ArrayList<AttrValue>();
		Iterator<ModelAttr> ite = mas.iterator();
		while(ite.hasNext()){
			ModelAttr modelAttr = ite.next();
			attvs.addAll(modelAttr.getAttrValues());
		}
		
		/*List<String> titles = new ArrayList<String>();
		List<List<AttrValue>> attrVs = new ArrayList<List<AttrValue>>();
		Iterator<Object> ite = attrList.iterator();
		
		while(ite.hasNext()){
			ModelAttr obj = ((JSONObject)ite.next()).toJavaObject(ModelAttr.class);
			titles.add(obj.getAttrTitle());
			attrVs.add(obj.getattrValues());
		}
		
		Criteria crit = new Criteria().where("attr_title").in(titles);
		Query query = Query.query(crit);
		
		mongoTemplate.updateMulti(query, , ModelAttr.class);*/
		modelAttrRepository.saveAll(mas);
		attrValueRepository.saveAll(attvs);
		System.out.println(modelAttrRepository.findAll());
	}
}
