<template>
  <p>Valor: {{ valor }}</p>
  <button @click="incrementa">Aumenta</button>
</template>

<script setup>
import { ref, watch } from 'vue'

const valor = ref(0)

function incrementa() {
  valor.value = valor.value + 1
}

watch(valor, () => {
  console.log("Mudou o valor da variavel")
})

watch(valor, (depois, antes) => {
  console.log(`Mudou o valor da variável: ${antes}, ${depois}`)
})
</script>