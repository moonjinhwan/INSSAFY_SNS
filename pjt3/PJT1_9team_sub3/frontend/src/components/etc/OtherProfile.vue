<template>
  <div id="wrap" @click="clickProfile">
    <div id="default-image" class="image" v-if="!imageUrl" />
    <div class="image" v-if="imageUrl" :style="{ backgroundImage: `url(${imageUrl})` }" />
  </div>
</template>

<script>
export default {
  name: 'Profile',
  props: {
    getUserImage: String,
  },
  data() {
    return {
      imageUrl: false,
    };
  },
  created() {
    if (this.getUserImage != '' && this.getUserImage != null && this.getUserImage != 'null') {
      this.imageUrl = this.getUserImage;
    }
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
  /* border-style: solid !important;
  border-width: 3px !important; */
  transition: border 0.5s ease;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-position: center;
  background-size: cover;
}
</style>
