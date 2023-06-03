package cmc.mellyserver.mellybatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobconfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job helloWorldJob() {
		return jobBuilderFactory.get("helloWorldJob")
			.incrementer(new RunIdIncrementer())
			.start(helloWorldStep())
			.build();
	}

	@JobScope
	@Bean
	public Step helloWorldStep() {
		return stepBuilderFactory.get("helloWorldStep")
			.tasklet(helloWorldTasklet())
			.build();
	}

	@StepScope
	@Bean
	public Tasklet helloWorldTasklet() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println("hello world batch");
				return RepeatStatus.FINISHED;
			}
		};
	}
}
