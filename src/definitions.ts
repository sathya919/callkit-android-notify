export interface callkitandroidPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
