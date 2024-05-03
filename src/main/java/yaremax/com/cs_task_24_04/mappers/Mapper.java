package yaremax.com.cs_task_24_04.mappers;

public interface Mapper<E, D> {
    E toEntity(D d);
    D toDto(E e);
}
