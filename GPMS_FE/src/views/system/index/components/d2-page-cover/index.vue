<template>
  <div class="d2-page-cover">
    <div class="d2-page-cover__logo">
      <!-- logo -->
      <img class="page-login--logo" src="./image/logo@2x.png">
      <slot/>
    </div>
    <p class="d2-page-cover__title"><b>GPMS</b> is Graduation Practice Management System</p>
    <p class="d2-page-cover__sub-title">优雅的<b>毕业实习管理系统</b></p>
    <p class="d2-page-cover__sub-title">课题《<b>数科院学生毕业实习管理系统的设计与实现</b>》 实现demo</p>
    <p class="d2-page-cover__sub-title">Front End by <b>D2 Admin {{ $version }}</b>, thanks! </p>
    <p class="d2-page-cover__sub-title">Powered By&nbsp;<b>陈昶</b></p>
    <p class="d2-page-cover__build-time">FINAL BUILD TIME {{ $buildTime }}</p>
    <slot name="footer"/>
    <!--    <a target="blank" href="https://github.com/d2-projects/d2-admin">-->
    <!--      <img-->
    <!--        style="position: absolute; top: 0; right: 0; border: 0; width: 150px;"-->
    <!--        src="./image/darkblue@2x.png"-->
    <!--        alt="Fork me on GitHub">-->
    <!--    </a>-->
  </div>
</template>

<style lang="scss" scoped>
.d2-page-cover {
  @extend %full;
  @extend %unable-select;
  display: flex;
  flex-flow: column nowrap;
  justify-content: center;
  align-items: center;

  .d2-page-cover__logo {
    img {
      width: 500px;
    }
  }

  .d2-page-cover__title {
    margin: 0px;
    margin-bottom: 20px;
    font-weight: bold;
    color: $color-text-main;
  }

  .d2-page-cover__sub-title {
    margin: 0px;
    margin-bottom: 5px;
    color: $color-text-normal;
  }

  .d2-page-cover__build-time {
    margin: 0px;
    margin-bottom: 10px;
    font-size: 12px;
    color: $color-text-placehoder;
  }
}
</style>
<script>
</script>
