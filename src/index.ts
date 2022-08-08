import { registerPlugin } from '@capacitor/core';

import type { callkitandroidPlugin } from './definitions';

const callkitandroid = registerPlugin<callkitandroidPlugin>('callkitandroid', {
  web: () => import('./web').then(m => new m.callkitandroidWeb()),
});

export * from './definitions';
export { callkitandroid };
