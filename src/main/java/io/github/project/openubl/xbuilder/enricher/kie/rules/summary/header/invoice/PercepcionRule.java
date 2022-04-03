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
package io.github.project.openubl.xbuilder.enricher.kie.rules.summary.header.invoice;

import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.isInvoice;
import static io.github.project.openubl.xbuilder.enricher.kie.rules.utils.Helpers.whenInvoice;

import io.github.project.openubl.xbuilder.content.models.standard.general.Invoice;
import io.github.project.openubl.xbuilder.content.models.standard.general.Percepcion;
import io.github.project.openubl.xbuilder.enricher.kie.AbstractHeaderRule;
import io.github.project.openubl.xbuilder.enricher.kie.RulePhase;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Consumer;

@RulePhase(type = RulePhase.PhaseType.SUMMARY)
public class PercepcionRule extends AbstractHeaderRule {

    @Override
    public boolean test(Object object) {
        return (
            isInvoice.test(object) &&
            whenInvoice
                .apply(object)
                .map(invoice -> invoice.getPercepcion() != null && invoice.getTotalImporte() != null)
                .orElse(false)
        );
    }

    @Override
    public void modify(Object object) {
        Consumer<Invoice> consumer = invoice -> {
            BigDecimal montoBase = invoice.getTotalImporte().getImporteSinImpuestos();
            BigDecimal porcentaje = Optional.ofNullable(invoice.getPercepcion().getPorcentaje()).orElse(BigDecimal.ONE);
            BigDecimal monto = montoBase.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal montoTotal = montoBase.add(monto);

            invoice.getPercepcion().setMontoBase(montoBase);
            invoice.getPercepcion().setPorcentaje(porcentaje);
            invoice.getPercepcion().setMonto(monto);
            invoice.getPercepcion().setMontoTotal(montoTotal);
        };
        whenInvoice.apply(object).ifPresent(consumer);
    }
}
