<template>
  <p>Valores: {{ valor.field }}, {{ valor.field2 }}</p>
  <button @click="incrementaCampo1">Aumenta 1</button>
  <button @click="incrementaCampo2">Aumenta 2</button>
</template>

<script setup>
import { reactive, watch } from 'vue'

const valor = reactive({ field: 0, field2: 0 })

function incrementaCampo1() {
  valor.field = valor.field + 1
}
function incrementaCampo2() {
  valor.field2 = valor.field2 + 1
}

watch(() => valor.field, () => {
  console.log("Mudou o valor do campo 1")
})
watch(() => valor.field2, () => {
  console.log("Mudou o valor do campo 2")
})
</script>