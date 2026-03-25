<script setup>
import {defineProps } from 'vue'

const props = defineProps({
  tasks: Object
});

function toggle(task) {
  task.done = !task.done;
}
</script>

<template>
  <ul>
    <li v-for="task in tasks">
      <input type="checkbox" @change="toggle(task)" :checked="task.done">
      <span :class="[task.done ? 'done':'']" @click="toggle(task)">{{ task.title }}</span>
    </li>
  </ul>
</template>