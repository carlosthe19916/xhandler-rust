/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.xbuilder.enricher.kie.rules.enrich.header.invoice;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

import io.github.project.openubl.xbuilder.content.catalogs.Catalog52;
import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Rule for {@link Invoice#direccionEntrega}
 */
@RulePhase(type = RulePhase.PhaseType.ENRICH)
public class DireccionDeEntregaRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
            isInvoice.test(object) &&
            whenInvoice.apply(object).map(documento -> documento.getDireccionEntrega() != null).orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = document -> {
            Catalog52 leyenda = Catalog52.VENTA_REALIZADA_POR_EMISOR_ITINERANTE;

            Map<String, String> leyendas = new HashMap<>(document.getLeyendas());
            leyendas.put(leyenda.getCode(), leyenda.getLabel());

            document.setLeyendas(leyendas);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
