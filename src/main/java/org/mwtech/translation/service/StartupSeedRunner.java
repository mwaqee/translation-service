package org.mwtech.translation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupSeedRunner implements CommandLineRunner {
  private final Seeder seeder;
  private final boolean doSeed;
  public StartupSeedRunner(Seeder seeder, @Value("${app.seed:false}") boolean doSeed){
    this.seeder = seeder; this.doSeed = doSeed;
  }
  @Override
  public void run(String... args) throws Exception {
    if (doSeed) {
      seeder.seed(120_000);
      System.out.println("Seeded 120k translation_values.");
    }
  }
}
