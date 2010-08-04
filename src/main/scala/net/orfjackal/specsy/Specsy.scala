package net.orfjackal.specsy

import net.orfjackal.specsy.junit.SpecsyJUnitRunner

class Specsy(testClass: Class[_ <: Spec]) extends SpecsyJUnitRunner(testClass)
