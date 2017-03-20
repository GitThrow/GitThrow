Vue.component('build-config-generator', {
    data: function () {
        return {
            buildCommand: '',
            artifactPath: '',
            testCommand: '',
            enableBuildSettings: false,
            enableTestSettings: false
        };
    },
    methods: {
        onDownload: function (e) {
            const json = {};
            if (this.enableBuildSettings) {
                json.buildSettings = {
                    buildCommand: this.toStringArray(this.buildCommand),
                    artifactPath: this.toStringArray(this.artifactPath)
                };
            }
            if (this.enableTestSettings) {
                json.testSettings = {
                    testCommand: this.toStringArray(this.testCommand)
                }
            }
            const content = JSON.stringify(json, undefined, 2);
            const blob = new Blob([content], {"type": "text/plain"});
            e.target.href = window.URL.createObjectURL(blob);
        },
        validate: function () {
            //出力する内容が無いケースを弾く
            if (!this.enableBuildSettings && !this.enableTestSettings) {
                return false;
            }
            if (this.enableBuildSettings) {
                if (this.toStringArray(this.buildCommand).length === 0) {
                    return false;
                }
                if (this.toStringArray(this.artifactPath).length === 0) {
                    return false;
                }
            }
            if (this.enableTestSettings) {
                if (this.toStringArray(this.testCommand).length === 0) {
                    return false;
                }
            }
            return true;
        },
        toStringArray: function (str) {
            return str.split('\n').filter(str => str.length !== 0);
        }
    },
    template: `
    <form>
      <div class="box-body">
          <div class="form-group">
            <div class="checkbox">
              <label>
                <input type="checkbox" v-model="enableBuildSettings">ビルド設定
              </label>
            </div>
          </div>
          <div class="form-group">
            <label>ビルドコマンド</label>
            <textarea rows="3" :disabled="!enableBuildSettings" v-model="buildCommand" placeholder="Command..." class="form-control">{{buildCommand}}</textarea>
          </div>
          <div class="form-group">
            <label>成果物のパス</label>
            <textarea rows="3" :disabled="!enableBuildSettings" v-model="artifactPath" placeholder="Paths..." class="form-control">{{artifactPath}}
            </textarea>
          </div>
          <hr>
          <div class="form-group">
            <div class="checkbox">
              <label>
                <input type="checkbox" v-model="enableTestSettings">テスト設定
              </label>
            </div>
          </div>
          <div class="form-group">
            <label>テストコマンド</label>
            <textarea rows="3" :disabled="!enableTestSettings" v-model="testCommand" placeholder="Command..." class="form-control">{{testCommand}}
            </textarea>
          </div>
      </div>
      <div class="box-footer">
        <a download="workbenchconfig.json" :class="{disabled: !validate()}" type="submit" class="btn btn-info pull-right" @click="onDownload($event)">Download</a>
      </div>
    </form>
`
});