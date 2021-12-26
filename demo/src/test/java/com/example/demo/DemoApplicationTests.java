package com.example.demo;


import com.example.DemoApplication;
import com.example.model.Account;
import com.example.model.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
class DemoApplicationTests {

    @Autowired
    private WebApplicationContext webContext;
    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void Login() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

    }

    @Test
    public void LoginFailed() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin123").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isUnauthorized()).andDo(MockMvcResultHandlers.log()).andReturn();

    }

    @Test
    public void createAccountSuccessfuly() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("123456");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);
        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        postResult.getResponse().getContentAsString();
        assertThat(postResult.getResponse().getContentAsString(), is("{\"identityid\":\"123456\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));


    }


    @Test
    public void createAccountAlreadyExisiting() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("123456789");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);
        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        postResult.getResponse().getContentAsString();
        assertThat(postResult.getResponse().getContentAsString(), is("{\"identityid\":\"123456789\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));
        var postResultExisted = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isConflict()).andReturn();

    }

    @Test
    public void ReadAccountSuccessfully() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("12345678910");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);
        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        postResult.getResponse().getContentAsString();
        assertThat(postResult.getResponse().getContentAsString(), is("{\"identityid\":\"12345678910\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));

        var getResult = mockMvc.perform(MockMvcRequestBuilders.get("/Account/" + "12345678910").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        assertThat(getResult.getResponse().getContentAsString(), is("{\"identityid\":\"12345678910\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));

    }


    @Test
    public void ReadAccountNotExist() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        var getResult = mockMvc.perform(MockMvcRequestBuilders.get("/Account/" + "1234567891011").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

    }


    @Test
    public void DeleteAccountSuccessfully() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("12345678910111213");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);
        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        postResult.getResponse().getContentAsString();
        assertThat(postResult.getResponse().getContentAsString(), is("{\"identityid\":\"12345678910111213\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));

        var deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete("/Account/" + "12345678910111213").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

    }


    @Test
    public void DeleteAccountNotExist() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        var getResult = mockMvc.perform(MockMvcRequestBuilders.delete("/Account/" + "12345678910111213aaa").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

    }

    @Test
    public void UpdateAccountSuccessfully() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("12345678910111213abc");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);
        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/Account").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        postResult.getResponse().getContentAsString();
        assertThat(postResult.getResponse().getContentAsString(), is("{\"identityid\":\"12345678910111213abc\",\"name\":\"123\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));

        Account accountupdated = new Account();
        accountupdated.setIdentityid("12345678910111213abc");
        accountupdated.setName("123456");
        accountupdated.setBirthday(new Date(90, 11, 31));
        accountupdated.setGender(Gender.male);
        var putResult = mockMvc.perform(MockMvcRequestBuilders.put("/Account/" + "12345678910111213abc").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(accountupdated))).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        assertThat(putResult.getResponse().getContentAsString(), is("{\"identityid\":\"12345678910111213abc\",\"name\":\"123456\",\"birthday\":\"1990-12-30T16:00:00.000+00:00\",\"gender\":\"male\"}"));

    }


    @Test
    public void UpdateAccountNotExist() throws Exception {

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "admin").param("password", "admin").contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)).andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.log()).andReturn();
        var Token = result.getResponse().getHeaders("token");

        Account account = new Account();
        account.setIdentityid("12345678910111213bbb");
        account.setName("123");
        account.setBirthday(new Date(90, 11, 31));
        account.setGender(Gender.male);

        var getResult = mockMvc.perform(MockMvcRequestBuilders.put("/Account/" + "12345678910111213bbb").header("Authorization", Token).contentType(MediaType.APPLICATION_JSON_UTF8).content(asJsonString(account))).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
