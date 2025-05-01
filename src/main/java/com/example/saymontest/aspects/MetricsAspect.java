package com.example.saymontest.aspects;

import com.example.saymontest.aspects.annotations.Metric;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MetricsAspect {
    private final MeterRegistry micrometerMetrics;

    /**
     * Аспект для сбора метрик: количество вызовов, ошибок и время выполнения методов.
     * Заменяет функционал TimingAspect.
     */
    @Around("@annotation(metric)")
    public Object measure(ProceedingJoinPoint pjp, Metric metric) throws Throwable {
        // Получаем имя метода или используем заданное в аннотации
        String methodName = pjp.getSignature().getName();
        String metricName = metric.name().isEmpty() ? methodName : metric.name();

        // Увеличиваем счетчик вызовов
        micrometerMetrics.counter("method.calls",
                "name", metricName,
                "tags", String.join(",", metric.tags())
        ).increment();

        // Замеряем время выполнения
        Timer.Sample sample = Timer.start(micrometerMetrics);

        try {
            return pjp.proceed();
        } catch (Throwable ex) {
            // Увеличиваем счетчик ошибок
            micrometerMetrics.counter("method.errors",
                    "name", metricName,
                    "tags", String.join(",", metric.tags())
            ).increment();
            throw ex;
        } finally {
            // Фиксируем время выполнения
            sample.stop(micrometerMetrics.timer("method.duration",
                    "name", metricName,
                    "tags", String.join(",", metric.tags())
            ));
        }
    }
}