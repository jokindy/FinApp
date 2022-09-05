package com.jokindy.finapp.operation;

import com.jokindy.finapp.account.AccountRepository;
import com.jokindy.finapp.exception.BalanceIsNegativeException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.jokindy.finapp.operation.OperationType.EXPENSE;

@Service
@AllArgsConstructor
@Transactional
public class OperationService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final ModelMapper modelMapper;

    public void save(OperationDto operationDto, long userId) {
        Operation operation = modelMapper.map(operationDto, Operation.class);
        if (operationDto.getType().equals(EXPENSE)) {
            operation.setValue(operation.getValue() * -1);
        }
        operation.setUserId(userId);
        operationRepository.save(operation);
        handleOperation(operation);
    }

    public List<Operation> getAll(long userId) {
        return operationRepository.findAllByUserIdOrderByCreatedDesc(userId);
    }

    private void handleOperation(Operation operation) {
        long accountId = operation.getAccountId();
        double balance = accountRepository.findBalanceById(accountId);
        balance = balance + operation.getValue();
        if (balance < 0) {
            throw new BalanceIsNegativeException();
        }
        accountRepository.updateBalanceById(balance, accountId);
    }
}
