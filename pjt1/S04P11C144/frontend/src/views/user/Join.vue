<!--
    가입하기는 기본적인 폼만 제공됩니다
    기능명세에 따라 개발을 진행하세요.
    Sub PJT I에서는 UX, 디자인 등을 포함하여 백엔드를 제외하여 개발합니다.
 -->
<template>
  <div class="user join wrapC">
    <h1>가입하기</h1>
    <div class="form-wrap">
      <div class="input-with-label">
        <input
          v-model="uid"
          id="uid"
          placeholder="닉네임을 입력하세요."
          type="text"
          v-bind:class="{ error: error.uid, complete: !error.uid && uid.length !== 0 }"
        />
        <label for="uid">닉네임</label>
        <div class="error-text" v-if="error.uid">{{ error.uid }}</div>
      </div>

      <div class="input-with-label">
        <input
          v-model="email"
          id="email"
          placeholder="이메일을 입력하세요."
          type="text"
          v-bind:class="{ error: error.email, complete: !error.email && email.length !== 0 }"
        />
        <label for="email">이메일</label>
        <div class="error-text" v-if="error.email">{{ error.email }}</div>
      </div>

      <div class="input-with-label">
        <input
          v-model="password"
          id="password"
          :type="passwordType"
          placeholder="비밀번호를 입력하세요."
          v-bind:class="{
            error: error.password,
            complete: !error.password && password.length !== 0,
          }"
        />
        <label for="password">비밀번호</label>
        <div class="error-text" v-if="error.password">{{ error.password }}</div>
      </div>

      <div class="input-with-label">
        <input
          v-model="passwordConfirm"
          :type="passwordConfirmType"
          id="password-confirm"
          placeholder="비밀번호를 다시한번 입력하세요."
          v-bind:class="{
            error: error.passwordConfirm,
            complete: !error.passwordConfirm && email.length !== 0,
          }"
        />
        <label for="password-confirm">비밀번호 확인</label>
        <div class="error-text" v-if="error.passwordConfirm">{{ error.passwordConfirm }}</div>
      </div>
    </div>

    <label>
      <input v-model="isTerm" type="checkbox" id="term" />
      <span>약관을 동의합니다.</span>
    </label>

    <span @click="termPopup = true">약관보기</span>

    <button class="btn-bottom" @click="join">가입하기</button>
  </div>
</template>

<script>
import * as EmailValidator from 'email-validator';
import PV from 'password-validator';
import axios from 'axios';
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  created() {
    this.component = this;

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
    uid: function(v) {
      this.checkForm();
    },
    password: function(v) {
      this.checkForm();
    },
    email: function(v) {
      this.checkForm();
    },
    passwordConfirm: function() {
      this.checkForm();
    },
  },
  methods: {
    checkForm() {
      if (this.uid.length >= 0 && this.uid.length <= 3) {
        this.error.uid = '닉네임 형식이 아닙니다.';
      } else this.error.uid = false;

      if (this.email.length >= 0 && !EmailValidator.validate(this.email))
        this.error.email = '이메일 형식이 아닙니다.';
      else this.error.email = false;

      if (this.password.length >= 0 && !this.passwordSchema.validate(this.password))
        this.error.password = '영문,숫자 포함 8 자리이상이어야 합니다.';
      else this.error.password = false;

      if (this.passwordConfirm.length == 0 || this.passwordConfirm != this.password)
        this.error.passwordConfirm = '비밀번호가 같지 않습니다.';
      else this.error.passwordConfirm = false;

      let isSubmit = true;
      Object.values(this.error).map((v) => {
        if (v) isSubmit = false;
      });
      this.isSubmit = isSubmit;
    },
    join: function() {
      axios
        .post(`${SERVER_URL}/user/confirm/join`, {
          uid: this.uid,
          password: this.password,
          email: this.email,
        })
        .then((response) => {
          if (response.data.res == '1') {
            this.$router.push('/user/loginSuccess');
          } else {
            alert('회원가입 실패');
          }
        });
    },
  },
  data: () => {
    return {
      uid: '',
      email: '',
      password: '',
      passwordSchema: new PV(),
      passwordConfirm: '',
      isTerm: false,
      isLoading: false,
      error: {
        email: false,
        password: false,
        uid: false,
        passwordConfirm: false,
        term: false,
      },
      isSubmit: false,
      passwordType: 'password',
      passwordConfirmType: 'password',
      termPopup: false,
    };
  },
};
</script>
