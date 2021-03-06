package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.repositores.UnitOfMeasureRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {
        private final UnitOfMeasureRepository repository;
        private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureCommandToUnitOfMeasure;

    public UnitOfMeasureServiceImpl(UnitOfMeasureRepository repository, UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureCommandToUnitOfMeasure) {
        this.repository = repository;
        this.unitOfMeasureCommandToUnitOfMeasure = unitOfMeasureCommandToUnitOfMeasure;
    }

    @Override
    public Set<UnitOfMeasureCommand> listAllUmos() {
        return StreamSupport.stream(repository.findAll()
                .spliterator(), false)
                .map(unitOfMeasureCommandToUnitOfMeasure::convert)
                .collect(Collectors.toSet());
    }
}
