package org.daelimie.test.daelimie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by YS on 2016-03-08.
 */
public class AlramAdapter extends BaseAdapter {
    private static final String TAG = "ButtonAdapter";
    private ArrayList<String> m_dep;
    private ArrayList<String> m_des;

    public AlramAdapter(ArrayList<String> m_List, ArrayList<String> m_Mac) {
        this.m_dep = m_List;
        this.m_des = m_Mac;
    }


    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return m_dep.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return m_dep.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        View view = convertView;
        ViewHolder holder;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( view == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_alram_item, parent, false);

            holder = new ViewHolder();

            holder.dep_locate = (TextView) view.findViewById(R.id.dep_locate);
            holder.des_locate = (TextView) view.findViewById(R.id.des_locate);

            view.setTag(holder);
        } else {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            holder = (ViewHolder)view.getTag();

        }

        // 각 뷰에 값넣기

        // TextView에 현재 position의 문자열 추가
        holder.dep_locate.setText(m_dep.get(position));
        holder.des_locate.setText(m_des.get(position));
/*
        // 버튼을 터치 했을 때 이벤트 발생
        holder.btn_test.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 클릭 : " + m_dep.get(pos) + "\n MAC_ADDR: " + m_des.get(pos), Toast.LENGTH_SHORT).show();
            }
        });

        // 리스트 아이템을 터치 했을 때 이벤트 발생
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 터치 시 해당 아이템 이름 출력, fid에 따른 뷰 띄워줌
                Toast.makeText(context, "리스트 클릭 : " + m_List.get(pos) + "\n MAC_ADDR: " + m_Mac.get(pos), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "리스트 아이템:" + m_Fid.get(pos) + "," + m_Mac.get(pos));
                Intent intent = null;
                switch (m_Fid.get(pos)) {
                    case 0:
                        intent = new Intent(context, ButtonRegPage.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 1:
                        intent = new Intent(context, Count.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 2:
                        intent = new Intent(context, Alarm.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 3:
                        intent = new Intent(context, Stopwatch.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 4:
                        intent = new Intent(context, Check.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 5:
                        intent = new Intent(context, DownTimer.class);
                        intent.putExtra("flag", "reset");

                        break;
                    case 6:
                        intent = new Intent(context, Message.class);
                        intent.putExtra("flag", "reset");

                        break;
                }


                intent.putExtra("mac_addr", m_Mac.get(pos));
                context.startActivity(intent);
            }
        });
*/
        /*
        // 리스트 아이템을 길게 터치 했을 떄 이벤트 발생
        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // 터치 시 해당 아이템 이름 출력
                Toast.makeText(context, "리스트 롱 클릭 : " + m_List.get(pos), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    */
        return view;
    }

    static class ViewHolder {
        TextView dep_locate;
        TextView des_locate;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg, String _mac) {
        m_dep.add(_msg);
        m_des.add(_mac);
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_dep.remove(_position);
        m_des.remove(_position);
    }


}
