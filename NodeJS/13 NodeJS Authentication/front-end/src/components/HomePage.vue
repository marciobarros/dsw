<template lang="html">
  <v-container>
    <v-row>
      <v-col cols="12">
          <div class="jumbotron text-center mt-4 mb-4">
            <h1 class="mb-4">Heading</h1>
            <p class="lead">Cras justo odio, dapibus ac facilisis in, egestas eget quam. Fusce dapibus, tellus ac cursus commodo, tortor mauris condimentum nibh, ut fermentum massa justo sit amet risus.</p>

            <p v-if="!$root.credentials">
              <router-link class="link" :to="{ name: 'login' }">
                <a class="btn btn-lg btn-success" href="#" role="button">Login</a>
              </router-link>
            </p>
          </div>
      </v-col>
    </v-row>
      
    <v-row>
      <v-col cols="6">
        <h4>Subheading</h4>
        <p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>

        <h4>Subheading</h4>
        <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>

        <h4>Subheading</h4>
        <p>Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. </p>
      </v-col>

      <v-col cols="6">
        <h4>Subheading</h4>
        <p>Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum. Donec id elit non mi porta gravida at eget metus. Maecenas faucibus mollis interdum.</p>

        <h4>Subheading</h4>
        <p>Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum. Morbi leo risus, porta ac consectetur ac, vestibulum at eros. Cras mattis consectetur purus sit amet fermentum.</p>

        <h4>Subheading</h4>
        <p>Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. Maecenas sed diam eget risus varius blandit sit amet non magna, consectetur ac, vestibulum at eros. </p>
      </v-col>
    </v-row>
  </v-container>

</template>

<script>
  export default {
  }
</script>

<style lang="css" scoped>
div.jumbotron {
  background: #eee;
  border-radius: 16px;
  padding: 4px 32px;
}
</style>