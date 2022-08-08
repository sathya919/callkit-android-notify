import { WebPlugin } from '@capacitor/core';

import type { callkitandroidPlugin } from './definitions';

export class callkitandroidWeb extends WebPlugin implements callkitandroidPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
