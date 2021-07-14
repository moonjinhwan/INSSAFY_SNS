<template>
  <div id="wrap" @click="clickProfile">
    <div id="default-image" class="image" v-if="!getUserImage" :style="{ borderColor: `${getGenerationColor}` }" />
    <div
      class="image"
      v-if="getUserImage"
      :style="{ backgroundImage: `url(${getUserImage})`, borderColor: `${getGenerationColor}` }"
    />
  </div>
</template>

<script>
import { mapGetters } from 'vuex';
export default {
  name: 'Profile',
  data() {
    return {
      imageUrl: '',
    };
  },
  computed: {
    ...mapGetters('auth', ['getUserImage', 'getGenerationColor']),
  },
  methods: {
    clickProfile: function() {
      this.$emit('clickProfile');
    },
  },
};
</script>

<style scoped>
#wrap {
  width: 100%;
  height: 100%;
}
#default-image {
  background-image: url('../../assets/images/default_profile.jpg');
}
.image:hover,
.image:active {
  border-width: 6px !important;
}
.image {
  border-style: solid !important;
  border-width: 3px !important;
  transition: border 0.5s ease;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-position: center;
  background-size: cover;
}
</style>
