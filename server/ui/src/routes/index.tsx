import { createFileRoute } from "@tanstack/react-router";
import { Content, PageSection } from "@patternfly/react-core";

export const Route = createFileRoute("/")({
  component: HomeComponent,
});

function HomeComponent() {
  return (
    <>
      <PageSection variant="default">
        <Content>
          <h1>Openubl</h1>
          <p>Fully hosted and managed service</p>
        </Content>
      </PageSection>
    </>
  );
}
