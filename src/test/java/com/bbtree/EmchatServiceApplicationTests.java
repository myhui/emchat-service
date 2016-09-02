package com.bbtree;

import com.bbtree.web.TestController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EmchatServiceApplication.class)
public class EmchatServiceApplicationTests {

	private MockMvc mvc;
	@Before
	public void setUp() throws Exception {
		mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
	}

	@Test
	public void getHello() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/test-im").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andReturn();
	}

	@Test
	public void contextLoads() {
		System.out.println(System.currentTimeMillis());
	}


	@Test
	public void test(){
		List<String>  ss = new ArrayList<>();
		ss.add("sdlkfjslfk");
		ss.add(null);

		for (String s :ss){

			System.out.println(s);
		}
	}


	@Test
	public void test2(){
		List<String> strList = new ArrayList<String>();
		strList.add("aa");
		strList.add("bb");
		String [] objs = new String[strList.size()];
		strList.toArray(objs);

		System.out.println(Arrays.toString(objs));
	}


}
