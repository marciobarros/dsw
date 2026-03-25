<template>
  <p>2 ^ {{ expoente }} = {{ potencia }}</p>
  <button @click="incrementa">Aumenta o expoente</button>
</template>

<script setup>
import { ref, computed } from 'vue'

const expoente = ref(0)

const potencia = computed(() => {
  return Math.pow(2, expoente.value);
});

function incrementa() {
  expoente.value = expoente.value + 1
}
</script>
