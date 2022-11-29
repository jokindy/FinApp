package com.jokindy.finapp.operation;

import com.jokindy.finapp.account.AccountService;
import com.jokindy.finapp.operation.dto.OperationDto;
import com.jokindy.finapp.operation.dto.OperationShortDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static com.jokindy.finapp.operation.OperationType.EXPENSE;

@Service
@AllArgsConstructor
@Transactional
public class OperationService {

    private final AccountService accountService;
    private final OperationRepository operationRepository;
    private final ModelMapper modelMapper;

    public void save(OperationDto operationDto, long userId) {
        Operation operation = modelMapper.map(operationDto, Operation.class);
        if (operationDto.getType().equals(EXPENSE)) {
            operation.setValue(operation.getValue() * -1);
        }
        operation.setUserId(userId);
        operationRepository.save(operation);
        accountService.handleOperation(operation);
    }

    public List<OperationShortDto> getAll(long userId) {
        List<Operation> operations = operationRepository.findAllByUserIdOrderByCreatedDesc(userId);
        return operations.stream()
                .map(p -> modelMapper.map(p, OperationShortDto.class))
                .collect(Collectors.toList());
    }
}
