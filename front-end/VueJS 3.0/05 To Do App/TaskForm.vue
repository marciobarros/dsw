<script setup>
import {ref} from 'vue'

const props = defineProps({
  tasks: Object
});

let newTask = ref("")

function add() {
  var task = { title: newTask.value, done: false };
  props.tasks.push(task);
  newTask.value = '';
}
</script>

<template>
  <div>
    <input type="text" v-model="newTask" @keyup.enter="add"/>
    <input type="button" value=" + " @click="add"/>
  </div>
</template>