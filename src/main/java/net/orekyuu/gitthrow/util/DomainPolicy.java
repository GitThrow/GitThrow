package net.orekyuu.gitthrow.util;

public interface DomainPolicy<T> {

    boolean check(T value);

    /**
     * 常にtrueを返すポリシー
     * @param <T> T
     * @return 常に正常
     */
    static <T> DomainPolicy empty() {
        return (DomainPolicy<T>) value -> true;
    }
}
