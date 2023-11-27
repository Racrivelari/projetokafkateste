from flask import Flask, request, jsonify
import mysql.connector
import logging

app = Flask(__name__)

# Configurações de logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Configurações do banco de dados MySQL
db = mysql.connector.connect(
    host="localhost",
    user="root",
    password="1q2w3e4r5t",
    database="kafka"
)

@app.route('/receber-mensagem', methods=['POST'])
def receber_mensagem():
    try:
        mensagem_json = request.json
        logger.info("Mensagem recebida na API Python: %s", mensagem_json)

        # Extraia os dados da mensagem, incluindo o novo campo 'status_processamento'
        cod_msg = mensagem_json.get('codMsg')
        num_ctrl_ccs = mensagem_json.get('numCtrlCCS')
        cnpj_base_ent_respons = mensagem_json.get('cnpjBaseEntRespons')
        cnpj_base_part = mensagem_json.get('cnpjBasePart')
        ag_if = mensagem_json.get('agIf')
        ct_dep = mensagem_json.get('ctDep')
        dt_ini = mensagem_json.get('dtIni')
        dt_fim = mensagem_json.get('dtFim')
        dt_hr_bc = mensagem_json.get('dtHrBC')
        dt_movto = mensagem_json.get('dtMovto')
        
        # Novo campo 'status_processamento' com valor padrão 'PENDENTE'
        status_processamento = mensagem_json.get('status_processamento', 'PENDENTE')

        # Insira ou atualize os dados na tabela do banco de dados
        cursor = db.cursor()
        query = (
            "INSERT INTO tbl_controle_msg "
            "(cod_msg, num_ctrl_ccs, cnpj_base_ent_respons, cnpj_base_part, ag_if, ct_dep, dt_ini, dt_fim, dt_hr_bc, dt_movto, status_processamento) "
            "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) "
            "ON DUPLICATE KEY UPDATE "
            "num_ctrl_ccs=VALUES(num_ctrl_ccs), cnpj_base_ent_respons=VALUES(cnpj_base_ent_respons), "
            "cnpj_base_part=VALUES(cnpj_base_part), ag_if=VALUES(ag_if), ct_dep=VALUES(ct_dep), "
            "dt_ini=VALUES(dt_ini), dt_fim=VALUES(dt_fim), dt_hr_bc=VALUES(dt_hr_bc), "
            "dt_movto=VALUES(dt_movto), status_processamento=VALUES(status_processamento)"
        )
        values = (cod_msg, num_ctrl_ccs, cnpj_base_ent_respons, cnpj_base_part, ag_if, ct_dep, dt_ini, dt_fim, dt_hr_bc, dt_movto, status_processamento)
        cursor.execute(query, values)
        db.commit()
        cursor.close()

        return jsonify({"status": "success"})
    except Exception as e:
        logger.error("Erro ao processar a mensagem na API Python. Detalhes: %s", e)
        return jsonify({"status": "error", "message": str(e)})



if __name__ == '__main__':
    app.run(debug=True, port=5000)
