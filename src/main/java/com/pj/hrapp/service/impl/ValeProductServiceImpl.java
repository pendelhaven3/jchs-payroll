package com.pj.hrapp.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pj.hrapp.dao.ValeProductRepository;
import com.pj.hrapp.exception.ConnectToMagicException;
import com.pj.hrapp.exception.NoMagicCustomerCodeException;
import com.pj.hrapp.model.Employee;
import com.pj.hrapp.model.Payslip;
import com.pj.hrapp.model.ValeProduct;
import com.pj.hrapp.service.ValeProductService;
import com.pj.hrapp.util.UrlUtil;

@Service
public class ValeProductServiceImpl implements ValeProductService {

	private static final Logger logger = LoggerFactory.getLogger(ValeProductServiceImpl.class);
	
	private static final String SEARCH_URL = "http://magic-db:8080/salesInvoice/search?";
	
	private RestTemplate restTemplate;
	@Autowired private ValeProductRepository valeProductRepository;
	
	@Override
	public List<ValeProduct> findUnpaidValeProductsByEmployee(Employee employee) {
		if (StringUtils.isEmpty(employee.getMagicCustomerCode())) {
			throw new NoMagicCustomerCodeException();
		}
		
		Map<String, String> params = new HashMap<>();
		params.put("customerCode", employee.getMagicCustomerCode());
		params.put("paid", "false");
		
		String url = SEARCH_URL + UrlUtil.mapToQueryString(params);
		
		try {
			List<ValeProduct> valeProducts = Arrays.asList(restTemplate.getForObject(url, ValeProduct[].class));
			Collections.sort(valeProducts, 
					(o1,o2) -> o1.getSalesInvoiceNumber().compareTo(o2.getSalesInvoiceNumber()));
			return valeProducts;
		} catch (RestClientException e) {
			logger.error(e.getMessage(), e);
			throw new ConnectToMagicException();
		}
	}

	@Transactional
	@Override
	public void addValeProductsToPayslip(List<ValeProduct> valeProducts, Payslip payslip) {
		for (ValeProduct valeProduct : valeProducts) {
			valeProduct.setPayslip(payslip);
			valeProductRepository.save(valeProduct);
		}
	}

	@Override
	public void markValeProductsAsPaid(List<ValeProduct> valeProducts) {
//		List<Long> salesInvoiceNumbers = valeProducts.stream()
//				.map(valeProduct -> valeProduct.getSalesInvoiceNumber())
//				.collect(Collectors.toList());
//		
//		String url = MARK_AS_PAID_URL + UrlUtil.mapToQueryString("salesInvoiceNumber", salesInvoiceNumbers);
//		
//		HttpClient httpClient = HttpClientBuilder.create().build();
//		try {
//			HttpResponse response = httpClient.execute(new HttpPost(url));
//			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				throw new ValeProductsNotMarkedException();
//			}
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
	}

}
