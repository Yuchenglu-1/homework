package com.wetech.demo.web3j.controller;

import com.wetech.demo.web3j.domain.Request;
import com.wetech.demo.web3j.domain.TransferFromRequest;
import com.wetech.demo.web3j.service.LYHTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/token")
@Slf4j
@RequiredArgsConstructor
public class LYHTokenController {
    private final LYHTokenService lyhtokenService;
    @PostMapping("/deploy")
    public CompletableFuture<ResponseEntity<Map<String, String>>> deploy() {
        return lyhtokenService.deployContract()
                .thenApply(address -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("contractAddress", address);
                    return ResponseEntity.ok(response);
                });
    }
    @PostMapping("/mint")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> mint(@RequestBody Request request) {

        log.info("Minting request received for account {} with value {}", request.getAccount(), request.getValue());

        CompletableFuture<TransactionReceipt> futureReceipt =
                lyhtokenService.mint(request.getAccount(), request.getValue());

        // 链式调用来处理异步结果
        return futureReceipt.thenApply(receipt -> {
            // 交易成功提交
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Minting transaction successfully submitted.");
            response.put("transactionHash", receipt.getTransactionHash());//获取交易哈希

            // 返回 202 Accepted，表示请求已被接受处理，但尚未完成
            return ResponseEntity.ok(response);

        }).exceptionally(ex -> {
            // 发生异常
            log.error("Minting failed for account {}: {}", request.getAccount(), ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to submit minting transaction: " + ex.getCause().getMessage());

            // 返回 500 服务器内部错误
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });
    }
    @PostMapping("/transfer")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> transfer(@RequestBody Request request) {
        log.info("Transfer request received for account {} with value {}", request.getAccount(), request.getValue());
          CompletableFuture<TransactionReceipt>Receipt = lyhtokenService.transfer(request.getAccount(), request.getValue());
          return Receipt.thenApply(receipt -> {
              Map<String, Object>response=new HashMap<>();
              response.put("status","success");
              response.put("message","Transfer transaction successfully submitted.");
              response.put("transactionHash",receipt.getTransactionHash());
              //返回
              return ResponseEntity.ok(response);
          }).exceptionally(ex -> {
              log.error("Transfer failed for account {}: {}", request.getAccount(), ex.getMessage());
              Map<String, Object> response = new HashMap<>();
              response.put("status", "error");
              response.put("message", "Failed to submit transfer transaction: " + ex.getCause().getMessage());
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
          });
    }
    @GetMapping("/balanceOf")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> balanceOf(@RequestParam String account) {
        log.info("Balance request received for account {}", account);
        CompletableFuture<BigInteger> balance = lyhtokenService.balanceOf(account);
        return balance.thenApply(value -> {
            Map<String,Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Balance successfully retrieved.");
            response.put("balance", value.toString());
            return ResponseEntity.ok(response);

        }).exceptionally(ex -> {
            log.error("Failed to retrieve balance for account {}: {}", account, ex.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to retrieve balance: " + ex.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        });

    }
    @PostMapping("/approve")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> approve(@RequestBody Request request) {
        log.info("Approve request received for account {} with value {}", request.getAccount(), request.getValue());
        CompletableFuture<TransactionReceipt> Receipt = lyhtokenService.approve(request.getAccount(), request.getValue());
        return Receipt.thenApply(receipt -> {
            Map<String,Object>response=new HashMap<>();
            response.put("Status:","success");
            response.put("messsage","approve account"+request.getAccount()+"value"+request.getValue());
            response.put("transaction",receipt.getTransactionHash());
            return ResponseEntity.ok(response);

        }).exceptionally(ex -> {
            log.info("error for approve for account{},message{}",request.getAccount(),ex.getMessage());
            Map<String,Object>response=new HashMap<>();
            response.put("status","error");
            response.put("Approve error",ex.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        });

    }
    @PostMapping("/transferFrom")
    public CompletableFuture<ResponseEntity<Map<String, Object>>> transferFrom(@RequestBody TransferFromRequest request) {
        log.info("TransferFrom request received for account {} with value {}", request.getFrom(), request.getValue());
        CompletableFuture<TransactionReceipt> Receipt = lyhtokenService.transferFrom(request.getFrom(), request.getTo(), request.getValue());
        return Receipt.thenApply(receipt -> {
            Map<String,Object>response=new HashMap<>();
            response.put("status","success");
            response.put("message","transferFrom account"+request.getFrom()+"value"+request.getValue());
            response.put("transaction",receipt.getTransactionHash());
            return ResponseEntity.ok(response);

        }).exceptionally(ex-> {
            log.info("error for transferFrom for account{},message{}",request.getFrom(),ex.getMessage());
            Map<String,Object>response=new HashMap<>();
            response.put("status","error");
            response.put("transferFrom error",ex.getCause().getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        });

    }
}
