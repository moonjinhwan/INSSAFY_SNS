<template>
  <div id="app" :style="{ background: gradient }">
    <div id="filter" :style="{ borderRadius: radius }"></div>
  </div>
</template>

<script>
// https://codepen.io/ClementRoche/pen/papMew
export default {
  name: 'GradientGenerator',
  props: {
    radius: String,
  },
  data() {
    return {
      colors: [
        { id: 0, hex: '#f12711', disabled: false },
        { id: 1, hex: '#f5af19', disabled: false },
      ],
      id: 2,
    };
  },
  methods: {
    generateGradient() {
      for (let i = 0; i < this.colors.length; i++) {
        if (this.colors[i].disabled === false) this.colors[i].hex = this.randomHex();
      }
    },
    randomHex() {
      return (
        '#' +
        Math.random()
          .toString(16)
          .slice(2, 8)
      );
    },
  },
  created() {
    this.generateGradient();
  },
  computed: {
    gradient() {
      let colors = 'linear-gradient(45deg';
      this.colors.forEach(function(e) {
        colors += ',' + e.hex;
      });
      colors += ')';
      return colors;
    },
  },
};
</script>

<style scoped>
#app {
  width: 100%;
  height: 100%;
}
#filter {
  width: 100%;
  height: 100%;
  background-color: #00000055;
}
</style>
