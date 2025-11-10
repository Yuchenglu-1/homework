package com.wetech.demo.web3j.service;

import com.wetech.demo.web3j.contracts.lyhtoken.LYHToKen;
import com.wetech.demo.web3j.contracts.simplestorage.SimpleStorage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class LYHTokenService {
    private final Web3j web3j;
    private final Credentials credentials;
    private final ContractGasProvider gasProvider;

    private LYHToKen contract;
    /**
     * -- GETTER --
     *  Get the address of the currently loaded contract
     *
     * @return the contract address
     */
    @Getter
    private String contractAddress;

    /**
     * Deploy the SimpleStorage contract to the blockchain
     * @return the address of the deployed contract
     */
    public CompletableFuture<String> deployContract() {
        log.info("Deploying SimpleStorage contract...");
        return LYHToKen.deploy(web3j, credentials, gasProvider)
                .sendAsync()
                .thenApply(contract -> {
                    this.contract = contract;
                    this.contractAddress = contract.getContractAddress();
                    log.info("LYHToken contract deployed to: {}", contractAddress);
                    return contractAddress;
                });
    }
    public CompletableFuture<TransactionReceipt> mint(String account, BigInteger value) {
        // 准备调用
        RemoteFunctionCall<TransactionReceipt> mintCall = contract.mint(account, value);

        // 异步发送，并直接返回这个 Future 对象
        return mintCall.sendAsync();
    }
    public CompletableFuture<TransactionReceipt> transfer(String account, BigInteger value) {
        // 创建调用
        RemoteFunctionCall<TransactionReceipt> transferCall = contract.transfer(account, value);

        // 异步发送，并直接返回这个 Future 对象
        return transferCall.sendAsync();
    }
    public CompletableFuture<BigInteger> balanceOf(String account) {
        // 创建调用
        RemoteFunctionCall<BigInteger>balanceOfcall = contract.balanceOf(account);

        // 异步发送，并直接返回这个 Future 对象
        return  balanceOfcall.sendAsync();

    }
    public CompletableFuture<TransactionReceipt> approve(String account, BigInteger value) {
        // 创建调用
        RemoteFunctionCall<TransactionReceipt> approveCall = contract.approve(account, value);

        // 异步发送，并直接返回这个 Future 对象
        return approveCall.sendAsync();
    }
    public CompletableFuture<TransactionReceipt> transferFrom(String from, String to, BigInteger value) {
        // 创建调用
        RemoteFunctionCall<TransactionReceipt> transferFromCall = contract.transferFrom(from, to, value);

        // 异步发送，并直接返回这个 Future 对象
        return transferFromCall.sendAsync();
    }
    // 在服务类中添加方法



}
