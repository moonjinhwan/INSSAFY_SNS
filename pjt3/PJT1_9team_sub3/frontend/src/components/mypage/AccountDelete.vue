<template>
  <v-dialog v-model="dialog" persistent max-width="400px">
    <template v-slot:activator="{ on, attrs }">
      <v-btn block depressed v-bind="attrs" v-on="on" class="pa-0">
        회원탈퇴
      </v-btn>
    </template>
    <v-card>
      <v-card-title id="header">
        <span class="headline">회원탈퇴 확인</span>
        <v-btn icon id="passwordTogle">
          <v-icon @click="passwordShow = !passwordShow">{{ passwordShow ? 'mdi-eye' : 'mdi-eye-off' }}</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-text>
        <v-container>
          <v-row>
            <!--비밀번호 -->
            <v-text-field
              v-model="password"
              id="password"
              label="비밀번호"
              hint="비밀번호를 입력해주세요."
              color="blue darken-4"
              :type="passwordShow ? 'text' : 'password'"
              required
            ></v-text-field>
          </v-row>
          <div class="error-text" :class="{ visible: error.password !== 0 }">
            {{ error.passwordConfirm }}
          </div>
        </v-container>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn text @click="dialog = false" class="r-desc footer-btn">
          취소
        </v-btn>
        <v-btn text disabled v-if="!pass" class="r-desc footer-btn">
          확인
        </v-btn>
        <v-btn text @click="submit" v-if="pass" class="r-desc footer-btn">
          확인
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
// 비밀번호 양식 체크
import PV from 'password-validator';
import * as EmailValidator from 'email-validator';

export default {
  name: 'AccoundDelete',
  data() {
    return {
      password: '',
      passwordSchema: new PV(),
      // 에러 메세지
      error: {
        passwordConfirm: '',
      },
      pass: false,

      // 비밀번호 변경 버튼
      dialog: false,
      // 비밀번호 텍스트 보여주기
      passwordShow: false,
    };
  },
  created() {
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();
  },
  watch: {
    password: function(v) {
      this.checkForm();
    },
    email: function(v) {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {
      // 비밀번호 양식 확인 - 여기서 기존의 비밀번호 가져와야한다
      if (this.password.length >= 0 && !this.passwordSchema.validate(this.password)) this.error.password = '영문,숫자 포함 8 자리이상이어야 합니다.';
      else this.error.password = false;

      let isSubmit = true;
      Object.values(this.error).map((v) => {
        if (v) isSubmit = false;
      });
      this.pass = isSubmit;
    },
    submit: function() {
      // this.$store.dispatch('auth/postPassword', this.password);
      this.$store.dispatch('auth/actDeleteUser', [this.password]);
      this.dialog = false;
    },
  },
};
</script>

<style scoped>
#header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
#passwordTogle {
  margin-right: -8px;
}

/* 폼체크 관련 */
.error-text {
  display: block;
  margin: 20px auto -30px;
  opacity: 0;
  text-align: center;
  font-size: 1rem;
  color: #ff0404;
}
.visible {
  opacity: 1;
}
.footer-btn {
  font-size: 18px;
}
</style>
