import { faTemperatureHigh } from '@fortawesome/free-solid-svg-icons';
import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
  // theme: { dark: true },
  theme: {
    themes: {
      light: {
        primary: '#0b2945',
        secondary: '#b0bec5',
        accent: '#ff0000',
        error: '#b71c1c',
      },
    },
  },
});
