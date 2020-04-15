package com.fersoft.mercado.produtoservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.fersoft.mercado.produtoservice.repository.ProdutoRepository;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

@Service
public class ProdutoService {
	private static final String GROUP = "products";
	private static final int TIMEOUT = 60000;

	ProdutoRepository produtoRepository;
	TicketService ticketService;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository,
			TicketService ticketService) {
		this.produtoRepository = produtoRepository;
		this.ticketService = ticketService;
	}
			
	public Map<String, Map<String, Object>> getProduct(Integer productId) {
        List<Callable<AsyncResponse>> callables = new ArrayList<>();
        callables.add(new BackendServiceCallable("product", getProductDetails(productId)));
        callables.add(new BackendServiceCallable("ticket", getTicket(productId)));
        return doBackendAsyncServiceCall(callables);
    }
    private static Map<String, Map<String, Object>> doBackendAsyncServiceCall(List<Callable<AsyncResponse>> callables) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        try {
            List<Future<AsyncResponse>> futures = executorService.invokeAll(callables);
            executorService.shutdown();
            executorService.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS);
            Map<String, Map<String, Object>> result = new HashMap<>();
            for (Future<AsyncResponse> future : futures) {
                AsyncResponse response = future.get();
                result.put(response.serviceKey, response.response);
            }
            return result;
        } catch (InterruptedException|ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    @Cacheable
    private HystrixCommand<Map<String, Object>> getProductDetails(Integer productId) {
        return new HystrixCommand<Map<String, Object>>(
                HystrixCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("getProductDetails"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionTimeoutInMilliseconds(TIMEOUT)
                        )
        ) {
            @Override
            protected Map<String, Object> run() throws Exception {
            	Map<String, Object> result = new HashMap<String, Object>();
                result.put("product", produtoRepository.findById(productId).get());
                return result;
            }
            @Override
            protected Map getFallback() {
                return new HashMap<>();
            }
        };
    }
    private HystrixCommand<Map<String, Object>> getTicket(Integer productId) {
        return new HystrixCommand<Map<String, Object>>(
                HystrixCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(GROUP))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("getTicket"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionTimeoutInMilliseconds(TIMEOUT)
                        )
        ) {
            @Override
            protected Map<String, Object> run() throws Exception {
            	Map<String, Object> result = new HashMap<String, Object>();
                result.put("ticket", ticketService.findByProdutoId(productId));
                return result;
            }
            @Override
            protected Map getFallback() {
                return new HashMap<>();
            }
        };
    }
    private static class AsyncResponse {
        private final String serviceKey;
        private final Map<String, Object> response;
        AsyncResponse(String serviceKey, Map<String, Object> response) {
            this.serviceKey = serviceKey;
            this.response = response;
        }
    }
    private static class BackendServiceCallable implements Callable<AsyncResponse> {
        private final String serviceKey;
        private final HystrixCommand<Map<String, Object>> hystrixCommand;
        public BackendServiceCallable(String serviceKey, HystrixCommand<Map<String, Object>> hystrixCommand) {
            this.serviceKey = serviceKey;
            this.hystrixCommand = hystrixCommand;
        }
        @Override
        public AsyncResponse call() throws Exception {
            return new AsyncResponse(serviceKey, hystrixCommand.execute());
        }
    }
}
